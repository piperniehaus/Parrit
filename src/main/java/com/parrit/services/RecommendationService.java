package com.parrit.services;

import com.parrit.entities.PairingHistory;
import com.parrit.entities.Person;
import com.parrit.entities.Space;
import com.parrit.entities.Workspace;
import com.parrit.utilities.CurrentTimeProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    CurrentTimeProvider currentTimeProvider;

    @Autowired
    public RecommendationService(CurrentTimeProvider currentTimeProvider) {
        this.currentTimeProvider = currentTimeProvider;
    }

    private static final int FULL_SPACE_SIZE = 2;

    public Workspace get(Workspace workspace, List<PairingHistory> workspacePairingHistories) {
        List<Person> floatingPeople = workspace.getPeople();
        List<Space> emptySpaces = getEmptySpaces(workspace);
        Map<Person, Space> availablePairs = getAvailablePairsMap(workspace);

        if (!availablePairs.isEmpty()) {
            Pair<Long, Map<Person, Person>> bestPairing = new ImmutablePair<>(Long.MAX_VALUE, new HashMap<>());
            Map<Person, List<Pair<Person, Timestamp>>> floatingPersonListMap = getFloatingPeopleListMap(floatingPeople, availablePairs, workspacePairingHistories);

            bestPairing = findBestPermutation(floatingPersonListMap, 0L, new HashMap<>(), bestPairing);

            Map<Person, Person> bestPairingMap = bestPairing.getValue();
            for (Person floatingPerson : bestPairingMap.keySet()) {
                Person availablePerson = bestPairingMap.get(floatingPerson);
                Space pairSpace = availablePairs.get(availablePerson);

                pairSpace.getPeople().add(floatingPerson);
                floatingPeople.remove(floatingPerson);
            }
        }

        while (!floatingPeople.isEmpty()) {
            Person floatingPerson = floatingPeople.remove(0);

            Space emptySpace = emptySpaces.get(0); //TODO: Check if there is an emptySpace, if not add one and use it
            emptySpace.getPeople().add(floatingPerson);

            if (emptySpace.getPeople().size() == FULL_SPACE_SIZE)
                emptySpaces.remove(emptySpace);
        }

        return workspace;
    }

    private Pair<Long, Map<Person, Person>> findBestPermutation(Map<Person, List<Pair<Person, Timestamp>>> theMap, long currentTotal, Map<Person, Person> currentPairing, Pair<Long, Map<Person, Person>> bestPairing) {
        if (theMap.isEmpty()) {
            if (currentTotal < bestPairing.getKey()) {
                bestPairing = new ImmutablePair<>(currentTotal, new HashMap<>(currentPairing));
            }
            return bestPairing;
        }

        Person currentFloatingPerson = theMap.entrySet().iterator().next().getKey();
        List<Pair<Person, Timestamp>> ListOfSortedPairingChoices = theMap.remove(currentFloatingPerson);

        for (Pair<Person, Timestamp> currentPairingChoice : ListOfSortedPairingChoices) {
            if (currentPairing.containsValue(currentPairingChoice.getKey()))
                continue;

            currentTotal += currentPairingChoice.getValue().getTime();
            currentPairing.put(currentFloatingPerson, currentPairingChoice.getKey());

            bestPairing = findBestPermutation(theMap, currentTotal, currentPairing, bestPairing);

            currentPairing.remove(currentFloatingPerson);
            currentTotal -= currentPairingChoice.getValue().getTime();
        }

        theMap.put(currentFloatingPerson, ListOfSortedPairingChoices);

        return bestPairing;
    }

    private List<Space> getEmptySpaces(Workspace workspace) {
        return workspace.getSpaces()
                .stream()
                .filter(space -> space.getPeople().isEmpty())
                .collect(Collectors.toList());
    }

    private Map<Person, Space> getAvailablePairsMap(Workspace workspace) {
        Map<Person, Space> availablePairs = new HashMap<>();

        workspace.getSpaces()
                .stream()
                .filter(space -> !space.getPeople().isEmpty() && space.getPeople().size() < FULL_SPACE_SIZE)
                .forEach(space -> availablePairs.put(space.getPeople().get(0), space));

        return availablePairs;
    }

    private Map<Person, List<Pair<Person, Timestamp>>> getFloatingPeopleListMap(List<Person> floatingPeople, Map<Person, Space> availablePairs, List<PairingHistory> workspacePairingHistories) {
        Map<Person, List<Pair<Person, Timestamp>>> floatingPersonListMap = new HashMap<>();

        for (Person floatingPerson : floatingPeople) {
            List<Pair<Person, Timestamp>> floatingPersonList = new ArrayList<>();

            List<PairingHistory> floatingPersonPairingHistories = getPairingHistoryForPerson(floatingPerson, workspacePairingHistories);

            for (Person availablePerson : availablePairs.keySet()) {

                List<PairingHistory> matchingPairingHistories = getPairingHistoryForPerson(availablePerson, floatingPersonPairingHistories);

                if (matchingPairingHistories.isEmpty()) {
                    floatingPersonList.add(new ImmutablePair<>(availablePerson, new Timestamp(0L)));
                } else {
                    PairingHistory mostRecentPairing = matchingPairingHistories.stream().max(Comparator.comparing(PairingHistory::getTimestamp)).get();
                    floatingPersonList.add(new ImmutablePair<>(availablePerson, mostRecentPairing.getTimestamp()));
                }

            }

            floatingPersonListMap.put(floatingPerson, floatingPersonList);
        }

        return floatingPersonListMap;
    }

    private List<PairingHistory> getPairingHistoryForPerson(Person person, List<PairingHistory> pairingHistories) {
        return pairingHistories
                .stream()
                .filter(pairingHistory -> pairingHistory.getPersonOne().equals(person) || pairingHistory.getPersonTwo().equals(person))
                .collect(Collectors.toList());
    }
}
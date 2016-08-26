var dataReducer = require('project/reducers/dataReducer.js');

describe("dataReducer", function() {
    it("should get the default state", function() {
        var stateBefore = undefined;
        var action = {};
        var stateAfter = {
            project: {
                id: 0,
                people: [],
                pairingBoards: []
            },
            projectSaveStatus: {
              lastSavedProject: {},
              projectSaved: false
            },
            pairingHistory: {
                pairingHistoryList: []
            }
        };

        expect(
            dataReducer(stateBefore, action)
        ).toEqual(stateAfter);
    });
});

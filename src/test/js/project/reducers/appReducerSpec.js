var appReducer = require('project/reducers/appReducer.js');

describe("appReducer", function() {
	it("should get the default state", function() {
		var stateBefore = undefined;
		var action = {};
		var stateAfter = {
            settings: {
                isNewPersonModalOpen: false,
                isNewPairingBoardModalOpen: false,
                isPairingHistoryPanelOpen: false,
                errorType: 0
			},
            data: {
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
            }
		};

		expect(
			appReducer(stateBefore, action)
		).toEqual(stateAfter);
	});
});

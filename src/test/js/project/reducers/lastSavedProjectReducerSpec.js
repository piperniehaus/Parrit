var deepFreeze = require('deep-freeze');
var lastSavedProjectReducer = require('project/reducers/lastSavedProjectReducer.js');

describe("lastSavedProjectReducer", function () {
  it("should get the default state", function () {
      var stateBefore = undefined;
      var action = {};
      var stateAfter = {};

      expect(
          lastSavedProjectReducer(stateBefore, action)
      ).toEqual(stateAfter);
  });

  describe("actions", function () {
    describe("UPDATE_LAST_SAVED_PROJECT", function () {
      it("should set the lastSavedProject to the passed in currentProject", function () {
        var stateBefore = {};

        var action = {
            type: "UPDATE_LAST_SAVED_PROJECT",
            currentProject: {shoobadooba: "doobadoowa"}
        };

        var stateAfter = {shoobadooba: "doobadoowa"};

        deepFreeze(stateBefore);
        deepFreeze(action);

        expect(
            lastSavedProjectReducer(stateBefore, action)
        ).toEqual(stateAfter);
      });
    });
  });
});

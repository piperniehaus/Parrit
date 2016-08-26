var deepFreeze = require('deep-freeze');
var projectSaveStatusReducer = require('project/reducers/projectSaveStatusReducer.js');

describe("projectSaveStatusReducer", function () {
  it("should get the default state", function () {
      var stateBefore = undefined;
      var action = {};
      var stateAfter = {
        lastSavedProject: {},
        projectSaved: false
      };

      expect(
          projectSaveStatusReducer(stateBefore, action)
      ).toEqual(stateAfter);
  });

  describe("actions", function () {
    describe("SAVE_PROJECT", function () {
      it("should set the lastSavedProject to the passed in currentProject", function () {
        var stateBefore = {
          lastSavedProject: {},
          projectSaved: false
        };

        var action = {
            type: "SAVE_PROJECT",
            currentProject: {shoobadooba: "doobadoowa"}
        };

        var stateAfter = {
            lastSavedProject: {shoobadooba: "doobadoowa"},
            projectSaved: true
        };

        deepFreeze(stateBefore);
        deepFreeze(action);

        expect(
            projectSaveStatusReducer(stateBefore, action)
        ).toEqual(stateAfter);
      });
    });
  });
});

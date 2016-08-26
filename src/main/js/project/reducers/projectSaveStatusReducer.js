var _ = require('lodash');

var projectSaveStatusReducer = function(state, action) {
	if(typeof state === 'undefined') {
		return {
      lastSavedProject: {},
		};
	}

    switch (action.type) {
      case "SAVE_PROJECT":
        var stateClone = _.cloneDeep(state);
        stateClone.lastSavedProject = action.currentProject
        stateClone.projectSaved = true
        return stateClone;
      default:
          return state;
    }
};

module.exports = projectSaveStatusReducer;

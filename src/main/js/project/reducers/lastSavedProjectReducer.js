var _ = require('lodash');

var lastSavedProjectReducer = function(state, action) {
	if(typeof state === 'undefined') {
		return {};
	}

    switch (action.type) {
      case "UPDATE_LAST_SAVED_PROJECT":
        return action.currentProject
      default:
          return state;
    }
};

module.exports = lastSavedProjectReducer;

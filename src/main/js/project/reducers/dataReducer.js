var Redux = require('redux');

var projectReducer = require('project/reducers/projectReducer.js');
var lastSavedProjectReducer = require('project/reducers/lastSavedProjectReducer.js');
var pairingHistoryReducer = require('project/reducers/pairingHistoryReducer.js');

var dataReducer = Redux.combineReducers({
    project: projectReducer,
    lastSavedProject: lastSavedProjectReducer,
    pairingHistory: pairingHistoryReducer
});

module.exports = dataReducer;

var Redux = require('redux');

var projectReducer = require('project/reducers/projectReducer.js');
var projectSaveStatusReducer = require('project/reducers/projectSaveStatusReducer.js');
var pairingHistoryReducer = require('project/reducers/pairingHistoryReducer.js');

var dataReducer = Redux.combineReducers({
    project: projectReducer,
    projectSaveStatus: projectSaveStatusReducer,
    pairingHistory: pairingHistoryReducer
});

module.exports = dataReducer;

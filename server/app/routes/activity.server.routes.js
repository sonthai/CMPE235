'use strict';

module.exports = function(app) {
	// Routing logic   
	var activity = require('../../app/controllers/activity.server.controller');
	
	app.route('/activity/record').post(activity.record);
};

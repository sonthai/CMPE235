'use strict';

module.exports = function(app) {
	// Routing logic   
	// Routing logic   
	var sensors = require('../../app/controllers/sensors.server.controller');

	app.route('/sensors/add').post(sensors.add);
	app.route('/sensors/update/:id').put(sensors.update);
	app.route('/sensors/show/:id').get(sensors.show);	
	app.route('/sensors/delete/:id').delete(sensors.deleteSensor);
	app.route('/sensors/list').get(sensors.list);

};

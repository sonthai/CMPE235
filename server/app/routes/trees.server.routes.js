'use strict';

module.exports = function(app) {
	// Routing logic   
	var trees = require('../../app/controllers/trees.server.controller');

	app.route('/trees/add').post(trees.add);
	app.route('/trees/update/:id').put(trees.update);
	app.route('/trees/show/:id').get(trees.show);	
	app.route('/trees/delete/:id').delete(trees.deleteTree);
	app.route('/trees/list').get(trees.list);
};

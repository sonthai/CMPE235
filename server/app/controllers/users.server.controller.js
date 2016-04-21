'use strict';

/**
 * Module dependencies.
 */
//var _ = require('lodash');

/**
 * Extend user's controller
 */
/*
module.exports = _.extend(
	require('./users/users.authentication.server.controller'),
	require('./users/users.authorization.server.controller'),
	require('./users/users.password.server.controller'),
	require('./users/users.profile.server.controller')
);*/
var _ = require('lodash'),
	//errorHandler = require('errors.server.controller.js'),
	mongoose = require('mongoose'),
	//passport = require('passport'),
	User = mongoose.model('User');

exports.login = function(req, res) {
	res.status(200).send({message: 'Login'});

};

exports.register =  function(req, res) {	
	res.status(200).send({message: 'Registration'});
	console.log('Body: ' + JSON.stringify(req.body));
}


'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.https.onCall((data, context) => {
  const author = data.author;
  const message = data.message;
  
  if ((!(typeof author === 'string') || author.length === 0) || (!(typeof message === 'string') || message.length === 0)) {
    throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
        'two arguments "author" and "message" which must both be strings.');
  }

  if (!context.auth) {
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
        'while authenticated.');
  }

  const uid = context.auth.uid;
  const name = context.auth.token.name || null;
  const picture = context.auth.token.picture || null;
  const email = context.auth.token.email || null;
  
  var messageToSend = {
    data: {
      author: author,
      message: message
    },
    topic: 'AllIn'
  };

  return admin.database().ref('/notifications').push({
    author: author,
    message: message
  }).then(() => {
      console.log('SENDING DOUBLEMENT DOUBLEMENT NOTIFICATION...');
      return admin.messaging().send(messageToSend);

  }).then(() => {
    console.log('New notifications received from ' + author);
    return messageToSend;
  }).catch((error) => {
    throw new functions.https.HttpsError('unknown', error.message, error);
  });
});
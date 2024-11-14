const mongoose = require('mongoose');

const connectDB = async () => {

    const dbUri = process.env.MONGO_URI || 'mongodb://mongo:27020/notification-db';
  try {
    await mongoose.connect(dbUri, {
    });
    console.log('Connected to MongoDB');
  } catch (error) {
    console.error('Error connecting to MongoDB:', error);
    process.exit(1); // Exit if connection fails
  }
};

module.exports = {connectDB};

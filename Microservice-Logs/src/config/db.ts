import mongoose from 'mongoose';

const connectDB = async () => {
  const dbURI = process.env.MONGO_URI || 'mongodb://localhost:27017/logServiceDB';
  try {
    await mongoose.connect(dbURI, {

    });
    console.log(`MongoDB connected to ${dbURI}`);
  } catch (err) {
    console.error('Database connection error:', err);
    process.exit(1);
  }
};

export default connectDB;

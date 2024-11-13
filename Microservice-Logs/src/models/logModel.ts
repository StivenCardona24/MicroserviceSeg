import mongoose, { Document, Schema } from 'mongoose';

// Interfaz que extiende Document para el tipado de Mongoose
export interface ILog extends Document {
  application: string;
  logType: 'INFO' | 'ERROR' | 'DEBUG';
  module: string;
  timestamp: Date;
  summary: string;
  description: string;
}

const logSchema: Schema = new Schema({
  application: { type: String, required: true },
  logType: { type: String, required: true },
  module: { type: String, required: true },
  timestamp: { type: Date, default: Date.now },
  summary: { type: String, required: true },
  description: { type: String, required: true },
});

export const Log = mongoose.model<ILog>('Log', logSchema);

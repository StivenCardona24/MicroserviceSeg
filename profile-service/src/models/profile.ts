import { Schema, model } from 'mongoose';

interface IProfile {
  userId: string;
  nickname: string;
  bio: string;
  personalUrl: string;
  isContactPublic: boolean;
  address: string;
  organization: string;
  country: string;
  socialLinks: { [key: string]: string }; // { twitter: '...', github: '...' }
}

const profileSchema = new Schema<IProfile>({
  userId: { type: String, required: true, unique: true },
  nickname: { type: String, required: true },
  bio: { type: String },
  personalUrl: { type: String },
  isContactPublic: { type: Boolean, default: false },
  address: { type: String },
  organization: { type: String },
  country: { type: String },
  socialLinks: { type: Map, of: String },
}, { timestamps: true });

export const Profile = model<IProfile>('Profile', profileSchema);

import axios from 'axios';
import { AuthResponse, LoginRequest, SignupRequest, Music, User } from '../types';

const API_BASE_URL = 'http://localhost:4000';

const api = axios.create({
  withCredentials:true,
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authAPI = {
  signup: async (data: SignupRequest): Promise<User> => {
    const response = await api.post('/auth/signup', data);
    return response.data;
  },

  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data;
  },
};

export const musicAPI = {
  getSongs: async (): Promise<Music[]> => {
    const response = await api.get("/music/getSongs");
    return response.data;
  },

  streamMusic: async (musicId: string, sessionId: string): Promise<void> => {
    await api.get(`/music/stream/${musicId}`, {
      headers: {
        "X-SESSION-ID": sessionId,
      },
    });
  },

  endStream: async (sessionId: string): Promise<void> => {
    await api.post("/music/stream/end", null, {
      headers: {
        "X-SESSION-ID": sessionId,
      },
    });
  },

  search: async (keyword: string): Promise<Music[]> => {
    const response = await api.get(`/music/search/${keyword}`);
    return response.data;
  },

  getMusicImage: async (musicId: string): Promise<string> => {
    try {
      const response = await api.get(`/music/image/${musicId}`, {
        responseType: "blob",
      });
      return URL.createObjectURL(response.data);
    } catch (error) {
      throw error;
    }
  },

  getStreamBlob: async (musicId: string, sessionId: string): Promise<Blob> => {
    const response = await api.get(`/music/stream/${musicId}`, {
      headers: {
        "X-SESSION-ID": sessionId,
      },
      responseType: "blob",
    });
    return response.data;
  },

  getBlob: async (musicId: string, sessionId: string): Promise<string> => {
    const response = await api.get(`/music/stream/${musicId}`, {
      responseType: "blob",
      headers: {
        "X-SESSION-ID": sessionId,
      },
    });

    return URL.createObjectURL(response.data);
  },

  searchByArtist: async (keyword: string): Promise<Music[]> => {
    const response = await api.get(`/music/search/artist/${keyword}`);
    return response.data;
  },

  searchByAlbum: async (keyword: string): Promise<Music[]> => {
    const response = await api.get(`/music/search/album/${keyword}`);
    return response.data;
  },
};

export default api;
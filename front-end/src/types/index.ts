export interface Music {
  id: string;
  musicName: string;
  artistName: string;
  createdDate: string;
  musicUrl: string;
  album: string;
  musicThumbnailUrl: string;
}

export interface User {
  email: string;
  userName: string;
}

export interface AuthResponse extends User {
  jwtToken: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  userName: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}
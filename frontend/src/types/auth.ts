export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string | null;
  refreshToken: string | null;
  username: string;
  email: string;
  role: string;
}
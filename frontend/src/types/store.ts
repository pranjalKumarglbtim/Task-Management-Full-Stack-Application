export interface AuthState {
  user: {
    id: number;
    username: string;
    email: string;
    role: string;
  } | null;
  tokens: {
    accessToken: string;
    refreshToken: string;
  } | null;
  isAuthenticated: boolean;
  login: (user: AuthState['user'], tokens: AuthState['tokens']) => void;
  logout: () => void;
}
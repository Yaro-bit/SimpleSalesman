// static/js/modules/auth-manager.js
import { Utils } from './utils.js';

export class AuthManager {
  constructor(apiService, appState) {
    this.api = apiService;
    this.state = appState;
    this.refreshTimer = null;
    this.initializeAuth();
  }

  initializeAuth() {
    this.checkAuthStatus();
    this.setupTokenRefresh();
    this.setupVisibilityHandler();
  }

  checkAuthStatus() {
    const token = sessionStorage.getItem('access_token');
    const username = sessionStorage.getItem('username');
    
    if (token && username) {
      this.updateUserDisplay(username);
      
      this.validateToken(token).catch(error => {
        console.warn('Token validation failed:', error);
        this.handleAuthFailure();
      });
    } else {
      this.handleAuthFailure();
    }
  }

  async validateToken(token) {
    if (!token) {
      throw new Error('No token provided');
    }

    try {
      const payload = this.parseJWT(token);
      
      if (!payload) {
        throw new Error('Invalid token format');
      }

      const now = Math.floor(Date.now() / 1000);
      if (payload.exp && payload.exp < now) {
        throw new Error('Token expired');
      }

      const isHealthy = await this.api.healthCheck();
      if (!isHealthy) {
        console.warn('Backend health check failed');
      }

      return true;
    } catch (error) {
      console.error('Token validation error:', error);
      throw error;
    }
  }

  parseJWT(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error parsing JWT:', error);
      return null;
    }
  }

  setupTokenRefresh() {
    const token = sessionStorage.getItem('access_token');
    if (!token) return;

    const payload = this.parseJWT(token);
    if (!payload || !payload.exp) return;

    const expirationTime = payload.exp * 1000;
    const refreshTime = expirationTime - Date.now() - (5 * 60 * 1000);

    if (refreshTime > 0) {
      this.refreshTimer = setTimeout(() => {
        this.attemptTokenRefresh();
      }, refreshTime);
    }
  }

  async attemptTokenRefresh() {
    const refreshToken = sessionStorage.getItem('refresh_token');
    
    if (!refreshToken) {
      console.warn('No refresh token available');
      this.handleAuthFailure();
      return;
    }

    try {
      const newTokens = await this.refreshTokens(refreshToken);
      
      if (newTokens) {
        this.storeTokens(newTokens);
        this.setupTokenRefresh();
        Utils.showNotification('Sitzung verlängert', 'Ihre Anmeldung wurde automatisch verlängert', 'success');
      } else {
        throw new Error('Token refresh failed');
      }
    } catch (error) {
      console.error('Token refresh error:', error);
      Utils.showNotification('Sitzung abgelaufen', 'Bitte melden Sie sich erneut an', 'warning');
      this.handleAuthFailure();
    }
  }

  async refreshTokens(refreshToken) {
    try {
      const response = await fetch('/auth/refresh', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ refreshToken })
      });

      if (response.ok) {
        return response.json();
      } else {
        throw new Error('Refresh request failed');
      }
    } catch (error) {
      console.error('Token refresh request error:', error);
      return null;
    }
  }

  storeTokens(tokens) {
    if (tokens.access_token) {
      sessionStorage.setItem('access_token', tokens.access_token);
    }
    
    if (tokens.refresh_token) {
      sessionStorage.setItem('refresh_token', tokens.refresh_token);
    }

    if (tokens.access_token) {
      try {
        const payload = this.parseJWT(tokens.access_token);
        if (payload) {
          const username = payload.preferred_username || payload.email || 'User';
          sessionStorage.setItem('username', username);
          sessionStorage.setItem('user_id', payload.sub || '');
          this.updateUserDisplay(username);
        }
      } catch (error) {
        console.error('Error parsing new token:', error);
      }
    }
  }

  updateUserDisplay(username) {
    const usernameElement = document.getElementById('username');
    if (usernameElement) {
      usernameElement.textContent = username;
    }
  }

  setupVisibilityHandler() {
    document.addEventListener('visibilitychange', () => {
      if (!document.hidden) {
        this.checkAuthStatus();
      }
    });
  }

  handleAuthFailure() {
    this.clearAuthData();
    
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = null;
    }

    if (!window.location.pathname.includes('login.html') && 
        !window.location.pathname.includes('/login')) {
      this.redirectToLogin();
    }
  }

  clearAuthData() {
    ['access_token', 'refresh_token', 'username', 'user_id'].forEach(key => {
      sessionStorage.removeItem(key);
      localStorage.removeItem(key);
    });
  }

  redirectToLogin() {
    sessionStorage.setItem('redirect_after_login', window.location.href);
    window.location.href = '/login.html';
  }

  logout(showConfirm = true) {
    if (showConfirm && !confirm('Möchten Sie sich wirklich abmelden?')) {
      return false;
    }

    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = null;
    }

    this.clearAuthData();
    sessionStorage.setItem('just_logged_out', 'true');
    window.location.href = '/logout';
    
    return true;
  }

  isAuthenticated() {
    return !!sessionStorage.getItem('access_token');
  }

  getUsername() {
    return sessionStorage.getItem('username') || 'User';
  }

  getUserId() {
    return sessionStorage.getItem('user_id') || '';
  }

  getToken() {
    return sessionStorage.getItem('access_token');
  }

  getTokenPayload() {
    const token = this.getToken();
    return token ? this.parseJWT(token) : null;
  }

  getSessionInfo() {
    const token = this.getToken();
    if (!token) return null;

    const payload = this.parseJWT(token);
    if (!payload) return null;

    return {
      username: this.getUsername(),
      userId: this.getUserId(),
      issuer: payload.iss,
      issuedAt: payload.iat ? new Date(payload.iat * 1000) : null,
      expiresAt: payload.exp ? new Date(payload.exp * 1000) : null,
      timeUntilExpiry: payload.exp ? (payload.exp * 1000 - Date.now()) : null
    };
  }

  isTokenExpiringSoon(minutesThreshold = 5) {
    const sessionInfo = this.getSessionInfo();
    if (!sessionInfo || !sessionInfo.timeUntilExpiry) return false;

    return sessionInfo.timeUntilExpiry < (minutesThreshold * 60 * 1000);
  }

  generateState() {
    return btoa(String.fromCharCode.apply(null, 
      crypto.getRandomValues(new Uint8Array(32))
    )).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
  }

  generateCodeVerifier() {
    return btoa(String.fromCharCode.apply(null, 
      crypto.getRandomValues(new Uint8Array(32))
    )).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
  }

  async generateCodeChallenge(verifier) {
    const encoder = new TextEncoder();
    const data = encoder.encode(verifier);
    const digest = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(digest));
    return btoa(String.fromCharCode.apply(null, hashArray))
      .replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
  }

  async initiateLogin(keycloakConfig) {
    const codeVerifier = this.generateCodeVerifier();
    const codeChallenge = await this.generateCodeChallenge(codeVerifier);
    const state = this.generateState();

    sessionStorage.setItem('code_verifier', codeVerifier);
    sessionStorage.setItem('oauth_state', state);

    const params = new URLSearchParams({
      response_type: 'code',
      client_id: keycloakConfig.clientId,
      redirect_uri: keycloakConfig.redirectUri,
      scope: 'openid profile email',
      state: state,
      code_challenge: codeChallenge,
      code_challenge_method: 'S256'
    });

    const authUrl = `${keycloakConfig.baseUrl}/realms/${keycloakConfig.realm}/protocol/openid-connect/auth?${params.toString()}`;
    
    return authUrl;
  }

  async handleAuthCallback(code, state, keycloakConfig) {
    const storedState = sessionStorage.getItem('oauth_state');
    if (state !== storedState) {
      throw new Error('Invalid state parameter');
    }

    const codeVerifier = sessionStorage.getItem('code_verifier');
    if (!codeVerifier) {
      throw new Error('Code verifier not found');
    }

    const tokenResponse = await fetch(
      `${keycloakConfig.baseUrl}/realms/${keycloakConfig.realm}/protocol/openid-connect/token`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          grant_type: 'authorization_code',
          code: code,
          redirect_uri: keycloakConfig.redirectUri,
          client_id: keycloakConfig.clientId,
          code_verifier: codeVerifier
        })
      }
    );

    if (!tokenResponse.ok) {
      const errorText = await tokenResponse.text();
      throw new Error(`Token exchange failed: ${errorText}`);
    }

    const tokens = await tokenResponse.json();
    
    this.storeTokens(tokens);
    
    sessionStorage.removeItem('code_verifier');
    sessionStorage.removeItem('oauth_state');
    
    return tokens;
  }

  debugAuth() {
    const sessionInfo = this.getSessionInfo();
    const isExpiringSoon = this.isTokenExpiringSoon();
    
    console.log('=== Auth Debug Info ===');
    console.log('Authenticated:', this.isAuthenticated());
    console.log('Session Info:', sessionInfo);
    console.log('Token expiring soon:', isExpiringSoon);
    console.log('Refresh timer active:', !!this.refreshTimer);
    
    return {
      isAuthenticated: this.isAuthenticated(),
      sessionInfo,
      isExpiringSoon,
      hasRefreshTimer: !!this.refreshTimer
    };
  }
}
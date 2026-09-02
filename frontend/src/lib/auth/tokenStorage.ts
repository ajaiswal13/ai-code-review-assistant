const TOKEN_KEY = 'access_token'

export const tokenStorage = {
    getToken(): string | null {
        return localStorage.getItem(TOKEN_KEY)
    },

    setToken(token: string): void {
        localStorage.setItem(TOKEN_KEY, token)
    },

    removeToken(): void {
        localStorage.removeItem(TOKEN_KEY)
    },

    hasToken(): boolean {
        return localStorage.getItem(TOKEN_KEY) !== null
    },
}
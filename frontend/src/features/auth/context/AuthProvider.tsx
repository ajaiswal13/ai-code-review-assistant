import { useMemo, useState, type ReactNode } from 'react'

import { authApi } from '../api/authApi'
import { tokenStorage } from '../../../lib/auth/tokenStorage'
import { AuthContext } from './AuthContext'

interface AuthProviderProps {
    children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [isAuthenticated, setIsAuthenticated] = useState(
        () => tokenStorage.hasToken(),
    )

    const login = async (email: string, password: string): Promise<void> => {
        const response = await authApi.login({
            email,
            password,
        })

        tokenStorage.setToken(response.accessToken)
        setIsAuthenticated(true)
    }

    const logout = (): void => {
        tokenStorage.removeToken()
        setIsAuthenticated(false)
    }

    const value = useMemo(
        () => ({
            isAuthenticated,
            login,
            logout,
        }),
        [isAuthenticated],
    )

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}
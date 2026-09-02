import { apiClient } from '../../../lib/api/apiClient'

import type {
    LoginRequest,
    LoginResponse,
    RegisterRequest,
    RegisterResponse,
} from '../types/auth.types'

export const authApi = {
    async login(request: LoginRequest): Promise<LoginResponse> {
        const response = await apiClient.post<LoginResponse>(
            '/auth/login',
            request,
        )

        return response.data
    },

    async register(request: RegisterRequest): Promise<RegisterResponse> {
        const response = await apiClient.post<RegisterResponse>(
            '/auth/register',
            request,
        )

        return response.data
    },
}
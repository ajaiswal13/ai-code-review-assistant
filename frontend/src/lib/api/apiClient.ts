import axios from 'axios'

import { tokenStorage } from '../auth/tokenStorage'

export const apiClient = axios.create({
    baseURL: '/api/v1',
    headers: {
        'Content-Type': 'application/json',
    },
})

apiClient.interceptors.request.use((config) => {
    const token = tokenStorage.getToken()

    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }

    return config
})
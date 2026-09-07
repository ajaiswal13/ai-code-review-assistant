import { fetchEventSource } from '@microsoft/fetch-event-source'

import { tokenStorage } from '../../../lib/auth/tokenStorage'

import type { ReviewStatusEvent } from '../types/reviews.types'

interface SubscribeOptions {
    onStatusChange: (event: ReviewStatusEvent) => void
    onError?: (error: unknown) => void
    signal?: AbortSignal
}

export async function subscribeToReviewEvents(
    reviewId: string,
    options: SubscribeOptions,
): Promise<void> {
    const token = tokenStorage.getToken()

    if (!token) {
        throw new Error('Authentication token is missing.')
    }

    await fetchEventSource(
        `/api/v1/reviews/${reviewId}/events`,
        {
            method: 'GET',

            headers: {
                Authorization: `Bearer ${token}`,
            },

            signal: options.signal,

            onopen: async (response) => {
                if (!response.ok) {
                    throw new Error(
                        `SSE connection failed with status ${response.status}`,
                    )
                }
            },

            onmessage(message) {
                if (message.event !== 'REVIEW_STATUS') {
                    return
                }

                const event = JSON.parse(
                    message.data,
                ) as ReviewStatusEvent

                options.onStatusChange(event)
            },

            onerror(error) {
                options.onError?.(error)
            },
        },
    )
}
import { apiClient } from '../../../lib/api/apiClient'

import type {
    CreateReviewRequest,
    Review,
} from '../types/reviews.types'

export const reviewsApi = {
    async createReview(request: CreateReviewRequest): Promise<Review> {
        const response = await apiClient.post<Review>('/reviews', request)

        return response.data
    },

    async getReviews(): Promise<Review[]> {
        const response = await apiClient.get<Review[]>('/reviews')

        return response.data
    },

    async getReview(reviewId: string): Promise<Review> {
        const response = await apiClient.get<Review>(
            `/reviews/${reviewId}`,
        )

        return response.data
    },
}
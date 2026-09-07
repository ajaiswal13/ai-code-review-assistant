import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'

import { reviewsApi } from '../api/reviewsApi'
import { subscribeToReviewEvents } from '../api/reviewEventsApi'
import type {
    Review,
    ReviewStatus,
} from '../types/reviews.types'

function getStatusStyles(status: ReviewStatus) {
    switch (status) {
        case 'COMPLETED':
            return 'bg-green-100 text-green-700'

        case 'PROCESSING':
            return 'bg-blue-100 text-blue-700'

        case 'PENDING':
            return 'bg-yellow-100 text-yellow-700'

        case 'FAILED':
            return 'bg-red-100 text-red-700'
    }
}

function getStatusLabel(status: ReviewStatus) {
    switch (status) {
        case 'COMPLETED':
            return 'Completed'

        case 'PROCESSING':
            return 'Processing'

        case 'PENDING':
            return 'Pending'

        case 'FAILED':
            return 'Failed'
    }
}

function getSeverityStyles(severity: string) {
    switch (severity.toUpperCase()) {
        case 'HIGH':
            return 'bg-red-100 text-red-700'

        case 'MEDIUM':
            return 'bg-yellow-100 text-yellow-700'

        case 'LOW':
            return 'bg-blue-100 text-blue-700'

        default:
            return 'bg-gray-100 text-gray-700'
    }
}

function formatDate(date: string) {
    return new Date(date).toLocaleString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
    })
}

export default function ReviewDetailPage() {
    const { reviewId } = useParams<{ reviewId: string }>()

    const [review, setReview] = useState<Review | null>(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState('')

    useEffect(() => {
        if (!reviewId) {
            return
        }
        const controller = new AbortController()

        const loadReview = async () => {
            try {
                setError('')

                const data = await reviewsApi.getReview(reviewId)

                setReview(data)

                return data
            } catch (error) {
                console.error('Failed to load review:', error)
                setError('Unable to load this review. Please try again.')
                return null
            } finally {
                setIsLoading(false)
            }
        }

        const start = async () => {
            const initialReview = await loadReview()

            if (!initialReview) {
                return
            }

            if (
                initialReview.status !== 'PENDING' &&
                initialReview.status !== 'PROCESSING'
            ) {
                return
            }

            try {
                await subscribeToReviewEvents(reviewId, {
                    signal: controller.signal,

                    onStatusChange: async (event) => {
                        try {
                            const updatedReview =
                                await reviewsApi.getReview(reviewId)

                            setReview(updatedReview)

                            if (
                                event.status === 'COMPLETED' ||
                                event.status === 'FAILED'
                            ) {
                                controller.abort()
                            }
                        } catch (error) {
                            console.error(
                                'Failed to refresh review:',
                                error,
                            )
                        }
                    },

                    onError: (error) => {
                        if (!controller.signal.aborted) {
                            console.error(
                                'Review SSE connection error:',
                                error,
                            )
                        }
                    },
                })
            } catch (error) {
                if (!controller.signal.aborted) {
                    console.error(
                        'Failed to subscribe to review events:',
                        error,
                    )
                }
            }
        }

        start()

        return () => {
            controller.abort()
        }
    }, [reviewId])

    if (isLoading) {
        return (
            <div className="flex min-h-[60vh] items-center justify-center">
                <p className="text-sm text-gray-500">
                    Loading review...
                </p>
            </div>
        )
    }

    if (error || !review) {
        return (
            <div className="mx-auto max-w-5xl px-6 py-8">
                <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center">
                    <p className="text-sm text-red-700">
                        {error || 'Review not found.'}
                    </p>

                    <Link
                        to="/reviews"
                        className="mt-4 inline-block text-sm font-medium text-gray-700 hover:text-gray-900"
                    >
                        ← Back to Reviews
                    </Link>
                </div>
            </div>
        )
    }

    return (
        <div className="mx-auto max-w-5xl px-6 py-8">
            <div className="mb-8">
                <Link
                    to="/reviews"
                    className="text-sm font-medium text-gray-500 transition hover:text-gray-900"
                >
                    ← Back to Reviews
                </Link>

                <div className="mt-5 flex items-start justify-between gap-6">
                    <div>
                        <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">
                            Programming Language
                        </p>

                        <h1 className="mt-1 text-3xl font-bold tracking-tight text-gray-900">
                            {review.language}
                        </h1>

                        <p className="mt-2 text-sm text-gray-500">
                            Created {formatDate(review.createdAt)}
                        </p>
                    </div>

                    <span
                        className={`rounded-full px-3 py-1 text-xs font-semibold ${getStatusStyles(
                            review.status,
                        )}`}
                    >
            {getStatusLabel(review.status)}
          </span>
                </div>
            </div>

            {review.status === 'PENDING' && (
                <div className="rounded-xl border border-yellow-200 bg-yellow-50 p-8 text-center">
                    <h2 className="text-lg font-semibold text-gray-900">
                        Review submitted
                    </h2>

                    <p className="mt-2 text-sm text-gray-600">
                        Your review is waiting to be processed.
                    </p>
                </div>
            )}

            {review.status === 'PROCESSING' && (
                <div className="rounded-xl border border-blue-200 bg-blue-50 p-8 text-center">
                    <h2 className="text-lg font-semibold text-gray-900">
                        Review in progress
                    </h2>

                    <p className="mt-2 text-sm text-gray-600">
                        AI is currently reviewing your code...
                    </p>
                </div>
            )}

            {review.status === 'FAILED' && (
                <div className="rounded-xl border border-red-200 bg-red-50 p-8 text-center">
                    <h2 className="text-lg font-semibold text-gray-900">
                        Review failed
                    </h2>

                    <p className="mt-2 text-sm text-red-700">
                        Something went wrong while processing your review.
                    </p>
                </div>
            )}

            {review.status === 'COMPLETED' && review.result && (
                <div className="space-y-8">
                    <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-gray-500">
                                    Review Score
                                </p>

                                <p className="mt-1 text-4xl font-bold text-gray-900">
                                    {review.result.score}
                                    <span className="text-xl font-medium text-gray-400">
                    /100
                  </span>
                                </p>
                            </div>
                        </div>

                        <div className="mt-6 border-t border-gray-100 pt-6">
                            <p className="text-sm font-semibold text-gray-900">
                                Summary
                            </p>

                            <p className="mt-2 text-sm leading-6 text-gray-600">
                                {review.result.summary}
                            </p>
                        </div>
                    </div>

                    <div>
                        <div className="mb-4">
                            <h2 className="text-xl font-bold text-gray-900">
                                Issues
                            </h2>

                            <p className="mt-1 text-sm text-gray-500">
                                Potential improvements identified by the AI review.
                            </p>
                        </div>

                        <div className="space-y-4">
                            {review.result.issues.map((issue) => (
                                <div
                                    key={issue.id}
                                    className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm"
                                >
                                    <div className="flex items-start justify-between gap-4">
                                        <div className="flex items-center gap-3">
                      <span
                          className={`rounded-full px-3 py-1 text-xs font-semibold ${getSeverityStyles(
                              issue.severity,
                          )}`}
                      >
                        {issue.severity}
                      </span>

                                            <span className="text-sm font-medium text-gray-500">
                        {issue.category}
                      </span>
                                        </div>

                                        {issue.line !== null && (
                                            <span className="text-xs font-medium text-gray-500">
                        Line {issue.line}
                      </span>
                                        )}
                                    </div>

                                    <p className="mt-4 text-sm font-semibold text-gray-900">
                                        {issue.message}
                                    </p>

                                    <div className="mt-4 rounded-lg bg-gray-50 p-4">
                                        <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">
                                            Suggestion
                                        </p>

                                        <p className="mt-2 text-sm leading-6 text-gray-600">
                                            {issue.suggestion}
                                        </p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import { reviewsApi } from '../api/reviewsApi'
import type { Review, ReviewStatus } from '../types/reviews.types'

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

function formatDate(date: string) {
    return new Date(date).toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
    })
}

function ReviewCard({ review }: { review: Review }) {
    return (
        <Link
            to={`/reviews/${review.id}`}
            className="group block rounded-xl border border-gray-200 bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-md"
        >
            <div className="flex items-start justify-between gap-4">
                <div>
                    <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">
                        Language
                    </p>

                    <h2 className="mt-1 text-xl font-semibold text-gray-900">
                        {review.language}
                    </h2>
                </div>

                <span
                    className={`rounded-full px-3 py-1 text-xs font-semibold ${getStatusStyles(
                        review.status,
                    )}`}
                >
          {getStatusLabel(review.status)}
        </span>
            </div>

            <div className="mt-6">
                {review.status === 'COMPLETED' && review.result ? (
                    <>
                        <p className="text-sm text-gray-500">Review Score</p>

                        <p className="mt-1 text-3xl font-bold text-gray-900">
                            {review.result.score}
                            <span className="text-lg font-medium text-gray-400">
                /100
              </span>
                        </p>
                    </>
                ) : review.status === 'PROCESSING' ? (
                    <p className="text-sm text-gray-600">
                        AI is currently reviewing your code...
                    </p>
                ) : review.status === 'PENDING' ? (
                    <p className="text-sm text-gray-600">
                        Waiting for review processing...
                    </p>
                ) : (
                    <p className="text-sm text-red-600">
                        Review processing failed.
                    </p>
                )}
            </div>

            <div className="mt-6 flex items-center justify-between border-t border-gray-100 pt-4">
                <p className="text-xs text-gray-500">
                    {formatDate(review.createdAt)}
                </p>

                <span className="text-sm font-medium text-gray-700 transition group-hover:text-gray-900">
          View Review →
        </span>
            </div>
        </Link>
    )
}

export default function ReviewsPage() {
    const [reviews, setReviews] = useState<Review[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState('')

    useEffect(() => {
        const loadReviews = async () => {
            try {
                setError('')

                const data = await reviewsApi.getReviews()

                setReviews(data)
            } catch (error) {
                console.error('Failed to load reviews:', error)
                setError('Unable to load your reviews.')
            } finally {
                setIsLoading(false)
            }
        }

        loadReviews()
    }, [])

    if (isLoading) {
        return (
            <div className="flex min-h-[60vh] items-center justify-center">
                <p className="text-sm text-gray-500">
                    Loading reviews...
                </p>
            </div>
        )
    }

    if (error) {
        return (
            <div className="rounded-xl border border-red-200 bg-red-50 p-6 text-center">
                <p className="text-sm text-red-700">{error}</p>
            </div>
        )
    }

    return (
        <div className="mx-auto max-w-7xl px-6 py-8">
            <div className="mb-8 flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold tracking-tight text-gray-900">
                        Your Reviews
                    </h1>

                    <p className="mt-2 text-sm text-gray-500">
                        Review and track your AI-powered code reviews.
                    </p>
                </div>

                <Link
                    to="/reviews/new"
                    className="rounded-lg bg-gray-900 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-gray-800"
                >
                    + New Review
                </Link>
            </div>

            {reviews.length === 0 ? (
                <div className="rounded-xl border border-dashed border-gray-300 bg-white px-6 py-16 text-center">
                    <h2 className="text-lg font-semibold text-gray-900">
                        No reviews yet
                    </h2>

                    <p className="mt-2 text-sm text-gray-500">
                        Submit your first piece of code to get an AI-powered review.
                    </p>

                    <Link
                        to="/reviews/new"
                        className="mt-6 inline-block rounded-lg bg-gray-900 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-gray-800"
                    >
                        Create your first review
                    </Link>
                </div>
            ) : (
                <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
                    {reviews.map((review) => (
                        <ReviewCard key={review.id} review={review} />
                    ))}
                </div>
            )}
        </div>
    )
}
import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'

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
    return new Date(date).toLocaleString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
    })
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

function StatusMessage({ status }: { status: ReviewStatus }) {
    switch (status) {
        case 'PENDING':
            return (
                <div className="rounded-xl border border-yellow-200 bg-yellow-50 p-6">
                    <h2 className="text-lg font-semibold text-yellow-900">
                        Review submitted
                    </h2>

                    <p className="mt-2 text-sm text-yellow-700">
                        Your review is waiting to be processed.
                    </p>
                </div>
            )

        case 'PROCESSING':
            return (
                <div className="rounded-xl border border-blue-200 bg-blue-50 p-6">
                    <h2 className="text-lg font-semibold text-blue-900">
                        AI is reviewing your code
                    </h2>

                    <p className="mt-2 text-sm text-blue-700">
                        Your code is currently being analyzed. This page will
                        eventually update automatically.
                    </p>
                </div>
            )

        case 'FAILED':
            return (
                <div className="rounded-xl border border-red-200 bg-red-50 p-6">
                    <h2 className="text-lg font-semibold text-red-900">
                        Review processing failed
                    </h2>

                    <p className="mt-2 text-sm text-red-700">
                        We were unable to complete the review. Please try
                        submitting the code again.
                    </p>
                </div>
            )

        case 'COMPLETED':
            return null
    }
}

function ReviewIssueCard({
                             issue,
                         }: {
    issue: Review['result'] extends infer T
        ? T extends { issues: (infer I)[] }
            ? I
            : never
        : never
}) {
    return (
        <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
            <div className="flex flex-wrap items-center gap-2">
        <span
            className={`rounded-full px-2.5 py-1 text-xs font-semibold uppercase ${getSeverityStyles(
                issue.severity,
            )}`}
        >
           {issue.severity}
        </span>

                <span className="rounded-full bg-gray-100 px-2.5 py-1 text-xs font-medium text-gray-600">
          {issue.category}
        </span>

                {issue.line !== null && (
                    <span className="text-xs text-gray-500">
            Line {issue.line}
          </span>
                )}
            </div>

            <p className="mt-4 text-sm font-medium leading-6 text-gray-900">
                {issue.message}
            </p>

            <div className="mt-4 rounded-lg bg-gray-50 p-4">
                <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                    Suggestion
                </p>

                <p className="mt-2 text-sm leading-6 text-gray-700">
                    {issue.suggestion}
                </p>
            </div>
        </div>
    )
}

export default function ReviewDetailPage() {
    const { reviewId } = useParams<{ reviewId: string }>()

    const [review, setReview] = useState<Review | null>(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState('')

    useEffect(() => {
        const loadReview = async () => {
            if (!reviewId) {
                setError('Review ID is missing.')
                setIsLoading(false)
                return
            }

            try {
                setError('')

                const data = await reviewsApi.getReview(reviewId)

                setReview(data)
            } catch (error) {
                console.error('Failed to load review:', error)
                setError('Unable to load this review. Please try again.')
            } finally {
                setIsLoading(false)
            }
        }

        loadReview()
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

    if (error) {
        return (
            <div className="mx-auto max-w-5xl px-6 py-8">
                <Link
                    to="/reviews"
                    className="text-sm font-medium text-gray-500 transition hover:text-gray-900"
                >
                    ← Back to Reviews
                </Link>

                <div className="mt-8 rounded-xl border border-red-200 bg-red-50 p-6 text-center">
                    <p className="text-sm text-red-700">{error}</p>
                </div>
            </div>
        )
    }

    if (!review) {
        return null
    }

    return (
        <div className="mx-auto max-w-5xl px-6 py-8">
            <Link
                to="/reviews"
                className="text-sm font-medium text-gray-500 transition hover:text-gray-900"
            >
                ← Back to Reviews
            </Link>

            {/* Header */}
            <div className="mt-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
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
                        className={`w-fit rounded-full px-3 py-1.5 text-xs font-semibold ${getStatusStyles(
                            review.status,
                        )}`}
                    >
            {getStatusLabel(review.status)}
          </span>
                </div>
            </div>

            {/* Pending / Processing / Failed */}
            {review.status !== 'COMPLETED' && (
                <div className="mt-6">
                    <StatusMessage status={review.status} />
                </div>
            )}

            {/* Completed Review */}
            {review.status === 'COMPLETED' && review.result && (
                <div className="mt-6 space-y-6">
                    {/* Score + Summary */}
                    <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
                        <div className="rounded-xl border border-gray-200 bg-white p-6 text-center shadow-sm">
                            <p className="text-sm font-medium text-gray-500">
                                Review Score
                            </p>

                            <p className="mt-3 text-5xl font-bold text-gray-900">
                                {review.result.score}
                                <span className="text-2xl font-medium text-gray-400">
                  /100
                </span>
                            </p>
                        </div>

                        <div className="rounded-xl border border-gray-200 bg-white p-6 md:col-span-2 shadow-sm">
                            <p className="text-sm font-semibold uppercase tracking-wide text-gray-500">
                                Summary
                            </p>

                            <p className="mt-3 text-sm leading-7 text-gray-700">
                                {review.result.summary}
                            </p>
                        </div>
                    </div>

                    {/* Issues */}
                    <div>
                        <div className="mb-4 flex items-center justify-between">
                            <div>
                                <h2 className="text-xl font-bold text-gray-900">
                                    Issues
                                </h2>

                                <p className="mt-1 text-sm text-gray-500">
                                    {review.result.issues.length}{' '}
                                    {review.result.issues.length === 1
                                        ? 'issue'
                                        : 'issues'}{' '}
                                    identified
                                </p>
                            </div>
                        </div>

                        {review.result.issues.length === 0 ? (
                            <div className="rounded-xl border border-green-200 bg-green-50 p-6">
                                <p className="text-sm font-medium text-green-800">
                                    No issues were identified in this review. 🎉
                                </p>
                            </div>
                        ) : (
                            <div className="space-y-4">
                                {review.result.issues.map((issue) => (
                                    <ReviewIssueCard
                                        key={issue.id}
                                        issue={issue}
                                    />
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    )
}
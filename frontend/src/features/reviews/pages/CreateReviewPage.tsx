import { useState, type SyntheticEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'

import { reviewsApi } from '../api/reviewsApi'

const MAX_CODE_LENGTH = 500_000

export default function CreateReviewPage() {
    const navigate = useNavigate()

    const [language, setLanguage] = useState('')
    const [code, setCode] = useState('')
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const handleSubmit = async (
        event: SyntheticEvent<HTMLFormElement>,
    ) => {
        event.preventDefault()

        setError('')

        if (!language.trim()) {
            setError('Please select a programming language.')
            return
        }

        if (!code.trim()) {
            setError('Please enter the code you want to review.')
            return
        }

        setIsSubmitting(true)

        try {
            const review = await reviewsApi.createReview({
                language: language.trim(),
                code,
            })

            navigate(`/reviews/${review.id}`)
        } catch (error) {
            console.error('Failed to create review:', error)
            setError('Unable to create the review. Please try again.')
        } finally {
            setIsSubmitting(false)
        }
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

                <h1 className="mt-5 text-3xl font-bold tracking-tight text-gray-900">
                    New Code Review
                </h1>

                <p className="mt-2 text-sm text-gray-500">
                    Submit your code and let AI analyze it for potential issues
                    and improvements.
                </p>
            </div>

            <form
                onSubmit={handleSubmit}
                className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm"
            >
                <div className="space-y-6">
                    <div>
                        <label
                            htmlFor="language"
                            className="block text-sm font-semibold text-gray-900"
                        >
                            Programming Language
                        </label>

                        <select
                            id="language"
                            value={language}
                            onChange={(event) => setLanguage(event.target.value)}
                            disabled={isSubmitting}
                            className="mt-2 w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-gray-500 focus:ring-2 focus:ring-gray-200 disabled:bg-gray-100"
                        >
                            <option value="">Select a language</option>
                            <option value="Java">Java</option>
                            <option value="TypeScript">TypeScript</option>
                            <option value="JavaScript">JavaScript</option>
                            <option value="Python">Python</option>
                            <option value="Go">Go</option>
                            <option value="C++">C++</option>
                            <option value="C#">C#</option>
                        </select>
                    </div>

                    <div>
                        <div className="flex items-center justify-between">
                            <label
                                htmlFor="code"
                                className="block text-sm font-semibold text-gray-900"
                            >
                                Code
                            </label>

                            <span className="text-xs text-gray-500">
                {code.length.toLocaleString()} /{' '}
                                {MAX_CODE_LENGTH.toLocaleString()}
              </span>
                        </div>

                        <textarea
                            id="code"
                            value={code}
                            onChange={(event) => setCode(event.target.value)}
                            disabled={isSubmitting}
                            maxLength={MAX_CODE_LENGTH}
                            placeholder="Paste your code here..."
                            spellCheck={false}
                            className="mt-2 min-h-[420px] w-full resize-y rounded-lg border border-gray-300 bg-gray-950 p-4 font-mono text-sm leading-6 text-gray-100 outline-none placeholder:text-gray-500 focus:border-gray-500 focus:ring-2 focus:ring-gray-200 disabled:opacity-60"
                        />
                    </div>

                    {error && (
                        <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3">
                            <p className="text-sm text-red-700">{error}</p>
                        </div>
                    )}

                    <div className="flex items-center justify-end gap-3 border-t border-gray-100 pt-6">
                        <Link
                            to="/reviews"
                            className="rounded-lg px-4 py-2.5 text-sm font-semibold text-gray-600 transition hover:bg-gray-100 hover:text-gray-900"
                        >
                            Cancel
                        </Link>

                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="rounded-lg bg-gray-900 px-5 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-60"
                        >
                            {isSubmitting ? 'Submitting...' : 'Review Code'}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    )
}
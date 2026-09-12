import { useState } from 'react'

import {
    uploadKnowledgeDocument,
} from '../api/knowledgeApi'

export default function KnowledgeUploadPage() {
    const [file, setFile] = useState<File | null>(null)
    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [isUploading, setIsUploading] = useState(false)
    const [successMessage, setSuccessMessage] = useState('')
    const [errorMessage, setErrorMessage] = useState('')

    async function handleSubmit(event: React.SyntheticEvent<HTMLFormElement>) {
        event.preventDefault()

        setSuccessMessage('')
        setErrorMessage('')

        if (!file) {
            setErrorMessage('Please select a PDF file.')
            return
        }

        setIsUploading(true)

        try {
            await uploadKnowledgeDocument({
                file,
                title,
                description,
                category: 'CODING_STANDARDS',
            })

            setSuccessMessage(
                'Coding standards uploaded successfully.',
            )

            setFile(null)
            setTitle('')
            setDescription('')
        } catch {
            setErrorMessage(
                'Failed to upload the coding standards document.',
            )
        } finally {
            setIsUploading(false)
        }
    }

    return (
        <div className="mx-auto max-w-3xl px-6 py-10">
            <div className="mb-8">
                <h1 className="text-2xl font-bold tracking-tight text-gray-900">
                    Upload Coding Standards
                </h1>

                <p className="mt-2 text-sm text-gray-600">
                    Upload a PDF containing coding guidelines that the AI
                    reviewer can use during code reviews.
                </p>
            </div>

            <form
                onSubmit={handleSubmit}
                className="space-y-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm"
            >
                <div>
                    <label
                        htmlFor="file"
                        className="mb-2 block text-sm font-medium text-gray-700"
                    >
                        PDF document
                    </label>

                    <input
                        id="file"
                        type="file"
                        accept="application/pdf"
                        onChange={(event) => {
                            setFile(event.target.files?.[0] ?? null)
                        }}
                        className="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-700 file:mr-4 file:rounded-md file:border-0 file:bg-gray-100 file:px-4 file:py-2 file:text-sm file:font-medium"
                    />

                    {file && (
                        <p className="mt-2 text-sm text-gray-500">
                            Selected: {file.name}
                        </p>
                    )}
                </div>

                <div>
                    <label
                        htmlFor="title"
                        className="mb-2 block text-sm font-medium text-gray-700"
                    >
                        Title
                    </label>

                    <input
                        id="title"
                        type="text"
                        value={title}
                        onChange={(event) => setTitle(event.target.value)}
                        placeholder="Java & Spring Coding Standards"
                        required
                        className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none focus:border-gray-500"
                    />
                </div>

                <div>
                    <label
                        htmlFor="description"
                        className="mb-2 block text-sm font-medium text-gray-700"
                    >
                        Description
                    </label>

                    <textarea
                        id="description"
                        value={description}
                        onChange={(event) => setDescription(event.target.value)}
                        placeholder="Coding guidelines for Java and Spring Boot applications"
                        rows={4}
                        required
                        className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm outline-none focus:border-gray-500"
                    />
                </div>

                <div>
                    <label
                        htmlFor="category"
                        className="mb-2 block text-sm font-medium text-gray-700"
                    >
                        Category
                    </label>

                    <select
                        id="category"
                        value="CODING_STANDARDS"
                        disabled
                        className="w-full rounded-lg border border-gray-300 bg-gray-50 px-3 py-2 text-sm text-gray-700"
                    >
                        <option value="CODING_STANDARDS">
                            Coding Standards
                        </option>
                    </select>
                </div>

                {successMessage && (
                    <div className="rounded-lg bg-green-50 px-4 py-3 text-sm text-green-700">
                        {successMessage}
                    </div>
                )}

                {errorMessage && (
                    <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">
                        {errorMessage}
                    </div>
                )}

                <button
                    type="submit"
                    disabled={isUploading}
                    className="w-full rounded-lg bg-gray-900 px-4 py-2.5 text-sm font-medium text-white transition hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
                >
                    {isUploading ? 'Uploading...' : 'Upload Coding Standards'}
                </button>
            </form>
        </div>
    )
}
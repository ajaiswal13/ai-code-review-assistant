import { apiClient } from '../../../lib/api/apiClient'

export type KnowledgeCategory = 'CODING_STANDARDS'

export interface UploadKnowledgeDocumentRequest {
    file: File
    title: string
    description: string
    category: KnowledgeCategory
}

export async function uploadKnowledgeDocument(
    request: UploadKnowledgeDocumentRequest,
) {
    const formData = new FormData()

    formData.append('file', request.file)
    formData.append('title', request.title)
    formData.append('description', request.description)
    formData.append('category', request.category)

    const response = await apiClient.post(
        '/knowledge/documents',
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        },
    )

    return response.data
}
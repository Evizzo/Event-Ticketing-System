import { apiClient } from './ApiClient'

// User ID to use until the implementation of Spring Security: 
// a1a2b6da-aa65-4f81-88e9-f2d36d7e0e6a

export const retrieveAllEvents
    = () => apiClient.get(`/event`)

export const purchaseEventTicket = (eventId: string, userId: string) => {
    return apiClient.post(`/event/${eventId}/ticket?userId=${userId}`)
    }

export const retrieveAllUserTickets = (userId: string) => {
    return apiClient.get(`/user/tickets/${userId}`)
}

export const refoundTicket = (ticketId: string, userId: string) => {
    return apiClient.delete(`/event/refund/${ticketId}?userId=${userId}&refundAmount=1`)
}
import {apiClient} from './ApiClient'; 

type Event = {
    name: string;
    date: string;
    location: string;
    description: string;
    capacity: number;
    ticketPrice: number;
  };

type RegisterRequest = {
    firstname: string;
    lastname: string;
    email: string;
    password: string;
};

export const executeRegistration = (userData: RegisterRequest) => {
    return apiClient.post(`/api/v1/auth/register`, userData);
};

export const retrieveAllEvents = (sortCriteria: string) => 
    apiClient.get(`/event?sortCriteria=${sortCriteria}`)


export const addNewEvent = (event: Event) => {
    return apiClient.post('/event', event)
}
      
export const purchaseEventTicket = (eventId: string) => {
    return apiClient.post(`/event/${eventId}/ticket`)
}

export const retrieveAllUserTickets = (sortCriteria: string) => {
    return apiClient.get(`/user/tickets?sortCriteria=${sortCriteria}`)
}

export const refoundTicket = (ticketId: string) => {
    return apiClient.delete(`/event/refund/${ticketId}?refundAmount=1`)
}

export const searchEventsByName = (eventName: string) => {
    return apiClient.get(`/event/search?name=${eventName}`)
}

export const retrieveEventById = (eventId: string) => {
    return apiClient.get(`/event/${eventId}`)
}
export const retrieveCurrentUser = () => {
    return apiClient.get(`/user/current`)
}
export const executeJwtAuthenticationService = (email: string, password: string) => {
    return apiClient.post(`/api/v1/auth/authenticate`, { email, password });
}

export const executeLogout = () => {
    return apiClient.post(`/api/v1/auth/logout`);
}

export const retrieveUserCredits = () => {
    return apiClient.get(`/user/credits`)
}

export const deleteCurrentUser = () => {
    return apiClient.delete(`/user/delete-current-user`)
}

export const retrieveAllPublishersEvents = (sortCriteria: string) => {
    return apiClient.get(`/event/published?sortCriteria=${sortCriteria}`)
}

export const deleteEvent = (eventId: string) => {
    return apiClient.delete(`/event/${eventId}`)
}

export const eventIsDone = (eventId: string) => {
    return apiClient.put(`/event/done/${eventId}`)
}

export const editEvent = (eventId: string, updatedEvent: Event) => {
    return apiClient.put(`/event/${eventId}`, updatedEvent)
}

export const retrieveTop3PopularEvents = () => {
    return apiClient.get("/event/popular")
}
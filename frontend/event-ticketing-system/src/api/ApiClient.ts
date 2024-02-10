import axios from 'axios'

export const apiClient = axios.create(
    {
        baseURL: 'http://localhost:8080' // --> Running without docker.
        // baseURL: import.meta.env.VITE_API_URL // --> Running with docker.
    }
);

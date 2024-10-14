import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/documents/');
}

const create = data => {
    return httpClient.post("/api/documents/", data);
}

const get = id => {
    return httpClient.get(`/api/documents/${id}`);
}

const update = data => {
    return httpClient.put('/api/documents/update', data);
}

const remove = id => {
    return httpClient.delete(`/api/documents/${id}`);
}
export default { getAll, create, get, update, remove };

import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/requests/');
}

const create = data => {
    return httpClient.post("/api/requests/", data);
}

const get = id => {
    return httpClient.get(`/api/requests/${id}`);
}

const update = data => {
    return httpClient.put('/api/requests/update', data);
}

const remove = id => {
    return httpClient.delete(`/api/requests/${id}`);

}


const simulate = data => {
    return httpClient.get("/api/requests/calculate",{params:{data}});
}
export default { getAll, create, get, update, remove };

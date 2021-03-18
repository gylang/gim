import user from './user';
import comm from './comm';
import socketApi from './socketApi';

export default {
    ...socketApi,
    ...user,
    ...comm,
}

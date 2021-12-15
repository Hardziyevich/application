package com.hardziyevich.app.controller;

final class Constant {

    private Constant() {
        throw new UnsupportedOperationException();
    }

    static class HttpMethod {

        private HttpMethod() {
            throw new UnsupportedOperationException();
        }

        static final String GET = "GET";
        static final String POST = "POST";
        static final String DELETE = "DELETE";
    }

    static class HttpResponseStatus {

        private HttpResponseStatus() {
            throw new UnsupportedOperationException();
        }

        static final int STATUS_OK = 200;
        static final int STATUS_CREATED = 201;
        static final int STATUS_NO_CONTENT = 204;
        static final int STATUS_NOT_FOUND = 404;

    }
}

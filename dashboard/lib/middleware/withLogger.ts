// middleware/withLogging.ts
import { NextFetchEvent, NextRequest } from "next/server";
import { MiddlewareFactory } from "./type";
import logger from '../logger';
export const withLogger: MiddlewareFactory = (next) => {
    return async (request: NextRequest, _next: NextFetchEvent) => {
        logger.debug("request", request.url);
        return next(request, _next);
    };
};

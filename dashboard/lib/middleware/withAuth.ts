import { NextFetchEvent, NextRequest, NextResponse } from "next/server";
import { MiddlewareFactory } from "./type";
import { headers as getHeaders } from "next/headers";
import configuration from '@/configuration.mjs';
import { withBasePath } from '..';
import logger from '../logger';

export const withAuth: MiddlewareFactory = (next) => {
  return async (request: NextRequest, _next: NextFetchEvent) => {
    if ((configuration.PathAlias.Admin.Pattern as RegExp).test(request.nextUrl.pathname) && configuration.PathAlias.Admin.Login != request.nextUrl.pathname) {
      if (getHeaders().has(configuration.AuthField)) {
        const token = getHeaders().get(configuration.AuthField)
        logger.debug("token", token)
        return next(request, _next);
      } else {
        return NextResponse.redirect(new URL(withBasePath(configuration.PathAlias.Admin.Login), request.url))
      }
    }
    return next(request, _next);
  };
};

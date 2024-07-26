import { stackMiddlewares } from '@/lib/middleware/stackMiddlewares';
import { withRoot } from "@/lib/middleware/withRoot"
import { withLogger } from "@/lib/middleware/withLogger"
import { withAuth } from './lib/middleware/withAuth';

const middlewares = [withLogger, withRoot, withAuth];

export default stackMiddlewares(middlewares);
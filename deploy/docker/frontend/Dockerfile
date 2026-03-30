# Frontend Dockerfile
FROM node:20-alpine AS base

WORKDIR /app

FROM base AS dependencies

COPY package*.json ./
RUN npm ci

FROM dependencies AS development

EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]

FROM dependencies AS build

COPY . .
RUN npm run build

FROM nginx:alpine AS production

COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

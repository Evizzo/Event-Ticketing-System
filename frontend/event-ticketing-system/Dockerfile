FROM node:18.12.0-alpine3.16 as builder
WORKDIR /app
COPY . .
RUN npm ci 
RUN npm run build
 
FROM nginx:1.23.2-alpine as production
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
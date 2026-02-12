# Multi-stage build for Angular application
FROM node:20-alpine AS build

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy source code and build the app
COPY . .
RUN npm run build

# Runtime stage using Nginx
FROM nginx:alpine

# Copy the build output to Nginx's serve directory
COPY --from=build /app/dist/media-planet-frontend /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]

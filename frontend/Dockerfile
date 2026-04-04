# STAGE 1: Bauen der React App in einem Node-Container
FROM node:20 AS builder
WORKDIR /app

# Abhängigkeiten kopieren und installieren
COPY package*.json ./
RUN npm install

# Quellcode kopieren und Build generieren (erstellt den Ordner /app/dist)
COPY . .
RUN npm run build

# STAGE 2: Ausliefern der App
FROM nginx:alpine
# Kopieren der fertigen HTML/JS/CSS Files in den NGINX-Server
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

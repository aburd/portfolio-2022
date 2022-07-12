FROM clojure
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app 
RUN lein deps
COPY . /usr/src/app
RUN lein uberjar
CMD ["lein", "run"]
EXPOSE 3001

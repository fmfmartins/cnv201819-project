#!/usr/bin/env bash

LB_URL=52.47.172.80
LB_PORT=8000


#FIRST PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-46-42.dat" -o /tmp/solveimg.png

#SECOND PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-02-27_09-56-18.dat" -o /tmp/solveimg.png

#THIRD PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-31.dat" -o /tmp/solveimg.png

#FOURTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-39.dat" -o /tmp/solveimg.png

#FIFTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-46.dat" -o /tmp/solveimg.png

#SIXTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-28-59.dat" -o /tmp/solveimg.png

#SEVENTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_512x512_2019-03-01_10-29-31.dat" -o /tmp/solveimg.png

#OCTAVE PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-28.dat" -o /tmp/solveimg.png

#NITH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-57-37.dat" -o /tmp/solveimg.png

#TENTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-58-44.dat" -o /tmp/solveimg.png

#ELEVENTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_16-59-31.dat" -o /tmp/solveimg.png

#TWELFTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-00-23.dat" -o /tmp/solveimg.png

#THIRTEENTH PIC
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=BFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=DFS&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png

curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=412&x1=512&y0=412&y1=512&xS=412&yS=412&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=480&yS=400&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=200&yS=200&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png
curl "http://${LB_URL}:${LB_PORT}/climb?w=512&h=512&x0=0&x1=512&y0=0&y1=512&xS=0&yS=0&s=ASTAR&i=datasets/RANDOM_HILL_1024x1024_2019-03-08_17-04-10.dat" -o /tmp/solveimg.png

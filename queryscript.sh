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
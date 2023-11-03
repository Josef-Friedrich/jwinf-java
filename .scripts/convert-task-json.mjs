#! /usr/bin/env node

import * as fs from 'node:fs'

import { task, data } from './task.mjs'

console.log(task)
console.log(data)

const result = {
  tiles: task.itemTypes,
  levels: data
}

const json = JSON.stringify(result, null, 2)

console.log(json)

fs.writeFileSync('.scripts/task.json', json)
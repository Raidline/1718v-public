import soma from './arith'
import {sub, mult} from './arith'
import * as arith from './arith'

import leftpad from 'left-pad'

console.log(arith)

document.write('change3')
document.write(`<p>40 + 2 = ${soma(40, 2)}</p>`)
document.write(`<p>40 + 2 = ${arith.default(40, 2)}</p>`)
document.write(`<p>44 - 2 = ${arith.sub(44, 2)}</p>`)
document.write(`<p>7 * 6 = ${arith.mult(7, 6)}</p>`)
document.write(`<p>44 - 2 = ${sub(44, 2)}</p>`)
document.write(`<p>7 * 6 = ${mult(7, 6)}</p>`)

document.write(`<p>leftpad('a', 5, '-') = ${leftpad('a', 5, '-')}</p>`)

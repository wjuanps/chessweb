/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global PF */

function conectarServidor(xhr, status, args, dlg) {
    if (args.validationFailed) {
        PF(dlg).jq.effect("shake", {
            times: 5
        }, 100);
    } else {
        PF(dlg).hide();
    }
}


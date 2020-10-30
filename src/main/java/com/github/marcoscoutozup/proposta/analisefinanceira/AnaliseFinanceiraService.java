package com.github.marcoscoutozup.proposta.analisefinanceira;

import com.github.marcoscoutozup.proposta.proposta.Proposta;
import com.google.gson.Gson;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnaliseFinanceiraService {

                //1
    private AnaliseFinanceiraClient analiseFinanceiraClient;
                //2
    private AnaliseFinanceiraResponse analiseFinanceiraResponse;

    private Logger logger;

    public AnaliseFinanceiraService(AnaliseFinanceiraClient analiseFinanceiraClient) {
        this.analiseFinanceiraClient = analiseFinanceiraClient;
        this.logger = LoggerFactory.getLogger(AnaliseFinanceiraService.class);;
    }
                                                        //3
    public void processarAnaliseFinanceiraDaProposta(Proposta proposta) {
        Assert.notNull(proposta, "A proposta avaliada não pode ser nula");
        logger.info("[ANALISE FINANCEIRA] Processando a análise financeira de proposta: {}", proposta.getId());
        Map request = new HashMap();
        request.put("documento", proposta.descriptografarDocumento());
        request.put("nome", proposta.getNome());
        request.put("idProposta", proposta.getId());

        //4
        try {
            analiseFinanceiraResponse = analiseFinanceiraClient.processaAnaliseFinanceira(request);
        } catch (FeignException e){ //5
            //6
            if(e.status() == 422){
                analiseFinanceiraResponse = new Gson().fromJson(e.contentUTF8(), AnaliseFinanceiraResponse.class);
            }
        }

        Assert.notNull(analiseFinanceiraResponse, "A resposta da análise financeira não pode ser nula");
        proposta.modificarStatusDaProposta(analiseFinanceiraResponse.getResultadoSolicitacao().toStatusDaProposta());
    }
}

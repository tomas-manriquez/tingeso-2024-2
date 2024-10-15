package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RequestRepository;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ClientRepository clientRepository;

    //METODOS PARA ACCEDER A CRUD

    //CREATE solicitud
    //Entrada: objeto RequestEntity
    //Salida: el mismo objeto de entrada. Como efecto secundario, se guarda tal objeto en la base de datos
    public RequestEntity saveRequest(RequestEntity request)
    {
        return requestRepository.save(request);
    }

    //READ todas las solicitudes
    //Entrada: nada
    //Salida: ArrayList de objetos RequestEntity, puede estar vacio
    public ArrayList<RequestEntity> getRequests()
    {
        return (ArrayList<RequestEntity>) requestRepository.findAll();
    }

    //READ solicitud por su id en la base de datos
    //Entrada: Long id
    //Salida: objeto DocumentEntity si se encuentra en la base de datos, en otro caso null
    public RequestEntity getRequestById(Long id)
    {
        Optional<RequestEntity> request = requestRepository.findById(id);
        return  request.orElse(null);
    }

    //READ solicitud por el rut del cliente dueño en base de datos
    //Entrada: String rut
    //Salida: objeto RequestEntity si se encuentra en la base de datos, en otro caso null
    public RequestEntity getRequestByClientRut(String rut)
    {
        Optional<RequestEntity> request = requestRepository.findByClientRut(rut);
        return request.orElse(null);
    }

    //UPDATE solicitud en cualquiera de sus atributos
    //Entrada: objeto RequestEntity con cambios
    //el mismo objeto. Como efecto secundario, se actualiza el objeto con este id en la base de datos
    //Por naturaleza de DocumentEntity y del negocio, se recomienda no usar.
    public RequestEntity updateRequest(RequestEntity request)
    {
        return requestRepository.save(request);
    }

    //DELETE de solicitud por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    public boolean deleteRequest(Long id) throws Exception
    {
        try
        {
            requestRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //METODOS PARA REGLAS DE NEGOCIO


    //P1: Simulacion de Credito Hipotecario
    //Calcula cuota mensual de credito segun datos entregados por usuario y formula de enunciado
    //Entrada: String rutClient, Long totalAmount (monto del prestamo, capital)...
    //... Float annualFee, Integer payTerm (plazo en años de pago)
    //Salida: Long simulation, =0L si algun input es incorrecto o usuario no es cliente
    public Long creditSimulation(String rutClient, String creditType, Long totalAmount, Float annualFee, Integer payTerm)
    {
        long simulation = 0L;
        Optional<ClientEntity> found = clientRepository.findByRut(rutClient);
        if(found.isPresent())
        {
            if (totalAmount > 0L)
            {
                if (annualFee > 0L)
                {
                    float monthlyFee = (annualFee/12)/100;
                    if(creditType.equals("vivienda1"))
                    {
                        if(payTerm <=30)
                        {
                            long totalPayTerms = payTerm*12;
                            simulation = ((Double) (totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1))).longValue();
                        }
                        return simulation;
                    }
                    else
                    {
                        if(creditType.equals("vivienda2"))
                        {
                            if(payTerm <=20)
                            {
                                long totalPayTerms = payTerm*12;
                                simulation = ( (Double) (totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1))).longValue();
                            }
                            return simulation;
                        }
                        else
                        {
                            if(creditType.equals("comercial"))
                            {
                                if(payTerm<=25)
                                {
                                    long totalPayTerms = payTerm*12;
                                    simulation = ((Double)(totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1))).longValue();
                                }
                                return simulation;
                            }
                            else
                            {
                                if(creditType.equals("remodelacion"))
                                {
                                    if(payTerm<=15)
                                    {
                                        long totalPayTerms = payTerm*12;
                                        simulation = ((Double)(totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1))).longValue();
                                    }
                                    return simulation;
                                }
                                else
                                {
                                    return simulation;
                                }
                            }
                        }
                    }
                }
                else
                {
                    return simulation;
                }
            }
            else
            {
                return simulation;
            }
        }
        else
        {
            return simulation;
        }
    };

    //P3: Solicitud de Credito
    //Entrada: RequestEntity request
    //Salida: 'true' si se registro la solicitud con exito, 'false' en otro caso
    //casos 'false' se generar por cliente solicitante que no esta registrado
    public boolean makeRequest(RequestEntity requestNew)
    {
        if (clientRepository.findByRut(requestNew.getClientRut()).isPresent())
        {
            if (requestNew.getType()!= null && requestNew.getMaxPayTerm() != null
            && requestNew.getAnnualInterest() != null && requestNew.getMaxFinanceAmount() != null
            && requestNew.getPropertyValue() != null && requestNew.getMonthlyCreditFee()!= null
            && requestNew.getMonthlyClientIncome()!=null && requestNew.getCurrentJobAntiquity()!=null
            && requestNew.getMonthlyDebt() != null && requestNew.getBankAccountBalance()!=null)
            {
                //solicitud tiene campos requeridos completados
                requestNew.setHasSufficientDocuments(false);
                requestNew.setHasGoodCreditHistory(null);
                requestNew.setIsSelfEmployed(null);
                requestNew.setHasGoodIncomeHistory(null);
                requestNew.setHasGoodBankAccountBalanceHistory(null);
                requestNew.setHasGoodDepositHistory(null);
                requestNew.setHasGoodBalanceAccountAgeRate(null);
                requestNew.setHasMadeBigWithdrawalsRecently(null);
                if (requestNew.getDocuments().isEmpty())
                {
                    //campos requeridos completados pero no ingresa documentos
                    //Se ingresa request en estado 'E2'
                    requestNew.setStatus("E2");

                    saveRequest(requestNew);
                    return true;
                }
                else
                {
                    //campos requeridos completados e ingresa documentos
                    //Se ingresa request en estado 'E3'
                    requestNew.setStatus("E3");
                    saveRequest(requestNew);
                    return true;
                }
            }
            else
            {
                //solicitud no tiene todos los campos requeridos completados
                //Se ingresa request en estado 'E1'
                requestNew.setStatus("E1");
                saveRequest(requestNew);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    //P4: Evaluacion de Credito
    //Entrada: RequestEntity request
    //Salida: nada. Como efecto secundario, se altera el 'status' del RequestEntity de entrada segun reglas de negocio
    public void requestEvaluation(RequestEntity request)
    {
        if (requestRepository.findById(request.getId()).isPresent())  //solicitud existe en Base de Datos
        {
            if(request.getStatus().equals("E2") && request.getDocuments().isEmpty())
            {
                //Solicitud en estado Pendiente de Documentacion y aun no tiene documentos, nada que evaluar
                return;
            }
            if(request.getStatus().equals("E7") || request.getStatus().equals("E8") || request.getStatus().equals("E9") || request.getStatus().equals("E6"))
            {
                //credito aceptado, rechazado, cancelado o en desembolso, nada que evaluar
                return;
            }
            else
            {
                if (request.getType() != null && request.getMaxPayTerm() != null
                        && request.getAnnualInterest() != null && request.getMaxFinanceAmount() != null
                        && request.getPropertyValue() != null && request.getMonthlyCreditFee() != null
                        && request.getMonthlyClientIncome() != null && request.getHasGoodCreditHistory() != null
                        && request.getCurrentJobAntiquity() != null && request.getIsSelfEmployed() != null
                        && request.getHasGoodIncomeHistory() != null
                        && request.getBankAccountBalance() != null && request.getHasGoodBankAccountBalanceHistory() != null
                        && request.getHasGoodDepositHistory() != null && request.getHasGoodBalanceAccountAgeRate() != null
                        && request.getHasMadeBigWithdrawalsRecently() != null)
                {
                    if(request.getStatus().equals("E1")) //En revision inicial pero se actualizaron los datos
                    {
                        if(request.isHasSufficientDocuments()) //actualizaron datos y tiene documentos, pasa a E3 evaluacion
                        {
                            request.setStatus("E3");
                            updateRequest(request);
                            return;
                        }
                        else    //actualizaron datos y no tiene documentos suficientes, pasa a E2 pendiente de documentacion
                        {
                            request.setStatus("E2");
                            updateRequest(request);
                            return;
                        }
                    }
                    request.setMonthlyDebt(totalCostMonthly(request));
                    float feeIncomeRate = (request.getMonthlyCreditFee().floatValue() / request.getMonthlyDebt().floatValue()) * 100;
                    if (feeIncomeRate > 0.35D) //Falla R1, solicitud rechazada
                    {
                        request.setStatus("E7");
                        updateRequest(request);
                        return;
                    }
                    if (!request.getHasGoodCreditHistory()) //Falla R2, solicitud rechazada
                    {
                        request.setStatus("E7");
                        updateRequest(request);
                        return;
                    }
                    if(request.getIsSelfEmployed())
                    {
                        if (!request.getHasGoodIncomeHistory()) //R3 no cumple condicion para trabajador independiente, solicitud rechazada
                        {
                            request.setStatus("E7");
                            updateRequest(request);
                        }
                    }
                    else
                    {
                        if (request.getCurrentJobAntiquity() < 12) //R3 no cumple condicion para empleado, solicitud rechazada
                        {
                            request.setStatus("E7");
                            updateRequest(request);
                        }
                    }
                    if( (request.getMonthlyDebt() + request.getMonthlyCreditFee()) > request.getMonthlyClientIncome()*0.5)
                    {
                        //Falla R4, solicitud rechazada
                        request.setStatus("E7");
                        updateRequest(request);
                    }
                    //switch case para R5
                    switch (request.getType())
                    {
                        case("vivienda1"):                              //condiciones de credito para primera vivienda
                            if(request.getMaxFinanceAmount() > 0.8f)    //si monto financiamiento excede el maximo, ajustar a ese valor
                            {
                                request.setMaxFinanceAmount(0.8f);
                            }
                            if(request.getMaxPayTerm() > 30)            //si plazo excede el maximo, ajustar a ese valor
                            {
                                request.setMaxPayTerm(30);
                            }
                            if(request.getAnnualInterest() < 3.5f)      //si tasa de interes es menor al minimo, ajustar a ese valor
                            {
                                request.setAnnualInterest(3.5f);
                            }
                            if(request.getAnnualInterest() > 5.0f)      //si tasa de interes es mayor al maximo, ajustar a ese valor
                            {
                                request.setAnnualInterest(5.0f);
                            }

                            updateRequest(request);
                            break;
                        case("vivienda2"):                              //condiciones de credito para segunda vivienda
                            if(request.getMaxFinanceAmount() > 0.7f)    //si monto financiamiento excede el maximo, ajustar a ese valor
                            {
                                request.setMaxFinanceAmount(0.7f);
                                updateRequest(request);
                            }
                            if(request.getMaxPayTerm() > 20)            //si plazo excede el maximo, ajustar a ese valor
                            {
                                request.setMaxPayTerm(20);
                            }
                            if(request.getAnnualInterest() < 4.0f)      //si tasa de interes es menor al minimo, ajustar a ese valor
                            {
                                request.setAnnualInterest(4.0f);
                            }
                            if(request.getAnnualInterest() > 6.0f)      //si tasa de interes es mayor al maximo, ajustar a ese valor
                            {
                                request.setAnnualInterest(6.0f);
                            }

                            updateRequest(request);
                            break;
                        case("comercial"):                              //condiciones de credito para propiedad comercial
                            if(request.getMaxFinanceAmount() > 0.6f)    //si monto financiamiento excede el maximo, ajustar a ese valor
                            {
                                request.setMaxFinanceAmount(0.6f);
                                updateRequest(request);
                            }
                            if(request.getMaxPayTerm() > 25)            //si plazo excede el maximo, ajustar a ese valor
                            {
                                request.setMaxPayTerm(25);
                            }
                            if(request.getAnnualInterest() < 5.0f)      //si tasa de interes es menor al minimo, ajustar a ese valor
                            {
                                request.setAnnualInterest(5.0f);
                            }
                            if(request.getAnnualInterest() > 7.0f)      //si tasa de interes es mayor al maximo, ajustar a ese valor
                            {
                                request.setAnnualInterest(7.0f);
                            }

                            updateRequest(request);
                            break;
                        case("remodelacion"):                           //condiciones de credito para remodelacion
                            if(request.getMaxFinanceAmount() > 0.5f)    //si monto financiamiento excede el maximo, ajustar a ese valor
                            {
                                request.setMaxFinanceAmount(0.5f);
                                updateRequest(request);
                            }
                            if(request.getMaxPayTerm() > 15)            //si plazo excede el maximo, ajustar a ese valor
                            {
                                request.setMaxPayTerm(15);
                            }
                            if(request.getAnnualInterest() < 4.5f)      //si tasa de interes es menor al minimo, ajustar a ese valor
                            {
                                request.setAnnualInterest(4.5f);
                            }
                            if(request.getAnnualInterest() > 6.0f)      //si tasa de interes es mayor al maximo, ajustar a ese valor
                            {
                                request.setAnnualInterest(6.0f);
                            }

                            updateRequest(request);
                            break;

                    }
                    //para R6
                    ClientEntity client = clientRepository.findByRut(request.getClientRut()).get();
                    int clientAge = LocalDate.now().getYear() - LocalDate.parse(client.getBirthday()).getYear();
                    if( (75- (clientAge + request.getMaxPayTerm())) >= 5) //Falla R6, solicitud rechazada
                    {
                        request.setStatus("E7");
                        updateRequest(request);
                    }
                    //para R7
                    String clientSavingCapacity = null; //'solida', 'moderada' o 'insuficiente'
                    int savingCapacityRules = 0;        //las reglas de R7 cumplidas
                    if(request.getBankAccountBalance() >= request.getPropertyValue()*0.1f)
                    {
                        //cumple regla R71, punto positivo
                        savingCapacityRules++;
                    } //si no entra en este if, falla regla R71, punto negativo

                    if(request.getHasGoodBankAccountBalanceHistory())
                    {
                        //cumple regla R72, punto positivo
                        savingCapacityRules++;
                    } //si no entra en este if, falla regla R72, punto negativo
                    if(request.getHasGoodDepositHistory())
                    {
                        //cumple regla R73, punto positivo
                        savingCapacityRules++;
                    } //si no entra en este if, falla regla R73, punto negativo
                    if(request.getHasGoodBalanceAccountAgeRate())
                    {
                        //cumple regla R74, punto positivo
                        savingCapacityRules++;
                    } //si no entra en este if, falla regla R74, punto negativo
                    if (!request.getHasMadeBigWithdrawalsRecently())
                    {
                        //cumple regla R75, punto positivo
                        savingCapacityRules++;
                    } //si no entra en este if, falla regla R75, punto negativo

                    //asignacion de 'clientSavingCapacity'
                    if(savingCapacityRules==5)
                    {
                        //cliente cumple las 5 reglas, capacidad de ahorro solida
                        clientSavingCapacity = "solida";
                    }
                    if(savingCapacityRules>=2 && savingCapacityRules<5)
                    {
                        //cliente cumple entre 2 y 4 reglas, capacidad de ahorro moderada, necesita revision adicional
                        clientSavingCapacity = "moderada";
                        request.setStatus("E3");
                        updateRequest(request);
                        return;
                    }
                    if(savingCapacityRules < 2)
                    {
                        //cliente cumple menos de 2 reglas, capacidad de ahorro insuficiente, se rechaza solicitud
                        clientSavingCapacity = "insuficiente";
                        request.setStatus("E7");
                        updateRequest(request);
                        return;
                    }

                    //solicitud cumple con condiciones requeridas R1-R7, pasa a estado E4 pre-aprobada
                    request.setStatus("E4");
                    updateRequest(request);
                    return;


                }
                else //falta algun dato que se debia entregar en Registrar Solicitud
                {
                    request.setStatus("E1");
                    updateRequest(request);
                    return;
                }
            }
        }
        else    //Request entregado no existe en BD
        {
            return;
        }
    }

    //Auxiliar de P5
    //Actualiza estado de solicitud a mano. Para cambiar a estados: E5, E6, E8, E9
    //Entrada: RequestEntity solicitud, String estado nuevo
    //Salida: 'true' si se realiza actualizacion exitosamente, 'false' en otro caso
    public boolean updateStatus(RequestEntity request, String newStatus)
    {
        if(requestRepository.findById(request.getId()).isPresent())
        {
            if(newStatus.equals("E5") || newStatus.equals("E6") || newStatus.equals("E8") || newStatus.equals("E9"))
            {
                //se actualiza estado, operacion termina exitosamente
                request.setStatus(newStatus);
                updateRequest(request);
                return true;
            }
            else
            {
                //estado nuevo no se puede asignar manualmente, se cancela operacion
                return false;
            }
        }
        else
        {
            //request no se encuentra en Base de Datos, se cancela operacion
            return false;
        }
    }

    //P5: Seguimiento de Solicitudes
    //Retorna el estado de una solicitud
    //Entrada: id de solicitud (Long)
    //Salida: estado de la solicitud (String)
    public String requestTracking (Long requestId)
    {
        Optional<RequestEntity> request = requestRepository.findById(requestId);
        if(request.isPresent())
        {
            if(request.get().getStatus() != null)
            {
                return request.get().getStatus();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return  null;
        }
    }

    //P5: Seguimiento de solicitudes
    //Actualiza el estado de una solicitud
    //Entrada: Long id de solicitud, String estado nuevo
    //Salida: "Actualizado" si se actualiza la solicitud exitosamente, "Cancelado: " + razon de error
    public String updateStatus(Long requestId, String newStatus)
    {
        if(requestRepository.findById(requestId).isPresent())
        {
            RequestEntity request = requestRepository.findById(requestId).get();
            request.setStatus(newStatus);
            requestRepository.save(request);

            return "Actualizado";
        }
        else
        {
            return "Cancelado: Solicitud no existe / no se encontro";
        }
    }


    //Auxiliar de P6: Calculo de Costos Totales
    //Calcula el costo total de la cuota mensual de cierto credito
    //Entrada: RequestEntity request, interesan parametros de entrada para P1, y lista de comisiones/seguros por agregar
    //Salida: Long cuota mensual que debe pagar el cliente (costo total de la cuota)
    public Long totalCostMonthly(RequestEntity request)
    {
        Long result = creditSimulation(
                request.getClientRut(),
                request.getType(),
                request.getPropertyValue() * request.getMaxPayTerm(),
                request.getAnnualInterest(),
                request.getMaxPayTerm());

        for(int i=0;i < request.getExtraFees().size(); i++)
        {
            if (request.getExtraFees().get(i) %1 == 0) //valor es entero
            {
                result = result + request.getExtraFees().get(i).longValue();
            }
            else //valor es porcentaje
            {
                long aux = ((Float) (result * request.getExtraFees().get(i))).longValue();
                result = result + aux;
            }
        }
        return result;
    }

    //P6: Calculo de Costos Totales
    //Calcula el costo total (base por credito + seguros + comisiones) de cierto credito
    //Entrada: RequestEntity
    //Salida: Long costo total del credito
    public Long totalCost(RequestEntity request)
    {
        return totalCostMonthly(request) * 12 * request.getMaxPayTerm();
    }
}

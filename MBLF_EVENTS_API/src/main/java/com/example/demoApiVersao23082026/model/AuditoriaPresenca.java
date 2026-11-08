package com.example.demoApiVersao23082026.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="auditoria_presencas")
public class AuditoriaPresenca {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(name="inscricao_id",nullable=false) private Integer inscricaoId;
 @Column(name="operador_id",nullable=false) private Integer operadorId;
 @Column(nullable=false,length=30) private String acao;
 @Column(nullable=false,length=500) private String motivo;
 @Column(name="data_acao",nullable=false) private LocalDateTime dataAcao=LocalDateTime.now();
 public void setInscricaoId(Integer v){inscricaoId=v;}
 public void setOperadorId(Integer v){operadorId=v;}
 public void setAcao(String v){acao=v;}
 public void setMotivo(String v){motivo=v;}
}
